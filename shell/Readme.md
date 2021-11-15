```
table_name="SSDATA"
maprdb_table="/user/mapr/100894/mytable/"+table_name
hive_external_table="default.SIIBS_"+table_name+"_CDC"
df = spark.loadFromMapRDB(maprdb_table)
schema = df.dtypes
new_schema = []
for a in schema:
  a = str(a)
  a = a.replace("'","").replace(",","").replace("null","string").replace("(","").replace(")","").replace("_id","id")
  new_schema.append(a)

new_schema = ",".join(new_schema)

hive_query = '''CREATE EXTERNAL TABLE '''+ hive_external_table+''' 
('''+new_schema+''') 
STORED BY "org.apache.hadoop.hive.maprdb.json.MapRDBJsonStorageHandler" 
TBLPROPERTIES("maprdb.table.name" = "'''+maprdb_table+'''","maprdb.column.id" = "id");'''
hive_query = hive_query.replace('\\n','')
print hive_query


hive> CREATE EXTERNAL TABLE default.mytable_CDC
    > (id string,age double,firstName string,lastName string)
    > STORED BY "org.apache.hadoop.hive.maprdb.json.MapRDBJsonStorageHandler"
    > TBLPROPERTIES("maprdb.table.name" = "/user/mapr/100894/mytable","maprdb.column.id" = "id");

 insert /user/mapr/100894/mytable --value '{"_id":"user0010", "firstName" : "Matt", "lastName" : "Porker" , "age" : 34 }' 


  CREATE EXTERNAL TABLE default.mytable2 (id string,age double,firstName string,lastName string) STORED BY "org.apache.hadoop.hive.maprdb.json.MapRDBJsonStorageHandler" TBLPROPERTIES("maprdb.table.name" = "/user/mapr/100894/mytable","maprdb.column.id" = "id");
```

sftp ivand@sftp.mapr.com: -P 115 -D /servdata/support/maprpatches/v6.1.0/rpm/mapr-patch-6.1.0.20180926230239.GA-20211026074111.x86_64.rpm
