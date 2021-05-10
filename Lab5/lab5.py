import boto3
from boto3.dynamodb.conditions import Key
from pprint import pprint
from decimal import Decimal
import re

pattern = re.compile('([^\s\w]|_)+')

# not using this but initially thought I might have to get all docids
docIds = []
def getDocsInBucket(bucket, prefix):
    s3 = boto3.resource('s3')
    my_bucket = s3.Bucket(bucket)
    for i in my_bucket.objects.filter(Prefix=prefix):
        item = i.key.split("/")[2].rstrip(".txt")
        docIds.append(item)

# using this method to perform any sort of string sanitization
def cleanUpInput(value):
    value = value.lower()
    return pattern.sub('', value)

# model part of "mvc"
def retrieveItem(term):
    res = boto3.resource('dynamodb')
    table = res.Table('HanksTFIDF')
    out = table.query(KeyConditionExpression=Key('term').eq(term))['Items']
    return out

# controller part of "mvc"
def computeTFIDF(inputLine):
    tfidfDocDict = {}
    tfidfTotal = {}
    inputLine = cleanUpInput(inputLine)
    items = inputLine.split()
    for i in items:
        res = retrieveItem(i)
        tfidfDocDict[i] = {}
        for docIditem in res:
            tfidfDocDict[docIditem['doc_id']] = Decimal(docIditem['tfidf'])
            if docIditem['doc_id'] not in tfidfTotal:
                tfidfTotal[docIditem['doc_id']] = Decimal(docIditem['tfidf'])
            else:
                tfidfTotal[docIditem['doc_id']] += Decimal(docIditem['tfidf'])
    for i in tfidfTotal:
        tfidfTotal[i] = int(tfidfTotal[i]/len(items))
    tfidfTotal = sorted(tfidfTotal.items(), key=lambda kv: kv[1], reverse=True)
    return tfidfTotal[:5]

# view part of "mvc"
def processQuery():
    print("Query", end=": ")
    line = input()
    if line == "":
        return None
    result = computeTFIDF(line)
    if result == None:
        print("unable to calculate for input")
        return None
    i = 1
    for item in result:
        print("{}. {} -- {}".format(i, item[0], item[1]))
        i += 1

    print()
    return True

def getTfidf():
    while True:
        res = processQuery()
        if res == None:
            return

getTfidf()
