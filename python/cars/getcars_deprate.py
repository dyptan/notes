import requests
import json
import csv

with open("aotos_combined.csv", "r") as data_file:
    brends = []
    for line in data_file.readlines()[1:]:
        brends.append(line.split(",", 1)[0])

unique_brends = set(brends)

myFile = open('deprecation_rates.csv', 'w')
fieldnames = ['Brand', 'Model', 'Deprecation_rate']
writer = csv.writer(myFile, delimiter=',', quotechar='"')
writer.writerow(fieldnames)

for brend in unique_brends:
    response=requests.get("https://www.themoneycalculator.com/vehicle-finance/ajax/?method=getDeprModelListForManu&makeurival={0}".format(brend.upper()))
    # models = models.append(
    for each in (response.json()['data']):
        writer.writerow([brend,each['displayname'],each['estim3yrdepr']])

myFile.close()
print("Writing complete")

