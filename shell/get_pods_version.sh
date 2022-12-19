kubectl get pods -l app=$1 -o jsonpath='{range .items[*]}{"\n"}{.metadata.name}{"\n"}{range .spec.containers[*]}{.name}{": "}{.image}{"\n"}{end}{end}' $2 $3
