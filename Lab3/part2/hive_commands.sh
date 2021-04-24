
DROP TABLE IF EXISTS doc_term_count;
CREATE EXTERNAL TABLE doc_term_count(doc_id STRING, term STRING, count int) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' STORED AS TEXTFILE LOCATION '/data-output/3-split-doc-term';


DROP TABLE IF EXISTS doc_length;
CREATE EXTERNAL TABLE doc_length(doc_id STRING, doc_length int) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' STORED AS TEXTFILE LOCATION '/data-output/2-term-count-document';


DROP TABLE IF EXISTS df;
CREATE EXTERNAL TABLE df(term STRING, df int) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' STORED AS TEXTFILE LOCATION '/data-output/4-df';


DROP TABLE IF EXISTS term_in_doc_count;
CREATE EXTERNAL TABLE term_in_doc_count(term_id STRING, count int) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' STORED AS TEXTFILE LOCATION '/data-output/1-term-count';

