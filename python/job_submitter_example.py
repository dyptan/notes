import datetime
import json
from pathlib import Path

from kubernetes import client, config


def read_json(config_path: str):
    with open(Path(config_path)) as f:
        settings = json.load(f)
        return settings


def create_job_template(docker_image, name, source_name):
    pull_secret = client.V1SecretReference(name='pull-secret')
    source_var = client.V1EnvVar(name='SOURCE', value=source_name)
    other_vars = client.V1EnvFromSource(config_map_ref=client.V1ConfigMapEnvSource('ccr-launcher-conf'))
    volume_mount_with_configs = client.V1VolumeMount(mount_path='/app/conf', name='configuration-files')
    volumes = client.V1Volume(name='configuration-files',
                    config_map=client.V1ConfigMapVolumeSource(name='ccr-job-conf'))
    container = client.V1Container(
        env=[source_var],
        env_from=[other_vars],
        name='app',
        volume_mounts=[volume_mount_with_configs],
        image=docker_image)

    template = client.V1PodTemplateSpec(
        metadata=client.V1ObjectMeta(labels={'name': name}),
        spec=client.V1PodSpec(
            volumes=[volumes],
            restart_policy='OnFailure',
            image_pull_secrets=[pull_secret],
            containers=[container]))

    spec = client.V1JobSpec(template=template)
    job = client.V1Job(
        api_version='batch/v1',
        kind='Job',
        metadata=client.V1ObjectMeta(name=job_name),
        spec=spec)
    return job


def submit_job(docker_image, name, source_name, namespace):
    api_instance = client.BatchV1Api()
    return api_instance.create_namespaced_job(body=create_job_template(docker_image, name, source_name),
                                              namespace=namespace)


if __name__ == '__main__':
    print('Loading kube config...')
    config.load_incluster_config()

    print('Loading app config...')
    app_conf = read_json('conf/conf.json')
    image_name = app_conf['template']['image']

    for source in app_conf['sources']:
        print('Lunching Job -> ' + source)
        app_name = app_conf['template']['name']
        job_name = f'{app_name}-{source}-{datetime.date.today()}'
        job_name = str.replace(job_name, '_', '-')
        submit_job(image_name, job_name, source, app_conf['namespace'])
