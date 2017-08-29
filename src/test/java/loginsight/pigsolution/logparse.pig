-- load the weblogs into a sequence of one element tuples
rawweblogs = LOAD 'logdata' USING TextLoader AS (line:chararray);
--user = LOAD '/loginsight/hvc/*' USING PigStorage(',') AS (userid:chararray,username:chararray);
weblogs = FOREACH rawweblogs GENERATE  FLATTEN(REGEX_EXTRACT_ALL(line, '(\\d{2}-\\d{2}-\\d{4}:\\d{2}:\\d{2}:\\d{2})\\s(\\w+)\\s(\\w+)\\s(\\w+)\\s(\\d{3}.\\d{3}.\\d{2}.\\d{2})')) AS (ts:chararray,user:chararray,reportname:chararray,reportexecutiontime:chararray,ip:chararray);

