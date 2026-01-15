- route:
    id: list-to-csv-with-marshaller
    from:
      uri: timer:start
      parameters:
        repeatCount: 1
      steps:
        - setBody:
            constant: '[ ["material", "quantity", "color"], ["acrylic", 2, "orange"],
              ["rayon", 3, "purple"], ["cashmere", 4, "grey"], ["denim", 5,
              "brown"],["acrylic", 2, "orange"],
              ["rayon", 3, "purple"], ["cashmere", 4, "grey"], ["denim", 5,
              "brown"],["acrylic", 2, "orange"],
              ["rayon", 3, "purple"], ["cashmere", 4, "grey"], ["denim", 5,
              "brown"], ["#FOOTER 001", 12, 14] ]'
        - unmarshal:
            json: {}
        - setProperty:
            name: originalList
            simple: ${body}
        - log:
            message: body:(${body})
        - setProperty:
            expression:
              method:
                beanType: java.lang.Integer
                method: sum(${body.size}, -2)
            name: recordCount
        - setProperty:
            expression:
              method:
                beanType: java.lang.Integer
                method: sum(${body.size}, -1)
            name: footerIndex
        - setProperty:
            name: footer
            simple: ${body[${exchangeProperty.footerIndex}]}
        - setProperty:
            name: fileId
            simple: ${exchangeProperty.footer[0].replace('#FOOTER ','')}
        - setProperty:
            name: expectedRecordCount
            simple: ${exchangeProperty.footer[1]}
        - setProperty:
            name: expectedChecksum
            simple: ${exchangeProperty.footer[2]}
        - log:
            message: "Record Count: ${exchangeProperty.recordCount}"
        - log:
            message: "Extracted Footer: ${exchangeProperty.footer}"
        - log:
            message: "File Id: ${exchangeProperty.fileId}"
        - log:
            message: "Expected record count: ${exchangeProperty.expectedRecordCount}"
        - log:
            message: "Expected checksum: ${exchangeProperty.expectedChecksum}"
        - choice:
            otherwise:
              steps:
                - log:
                    loggingLevel: ERROR
                    message: "Validation Failed: Record count (${exchangeProperty.recordCount}) does
                      not match expected
                      (${exchangeProperty.expectedRecordCount})"
            when:
              - steps:
                  - log: "Validation Success: Record counts match"
                simple: ${exchangeProperty.recordCount} == ${exchangeProperty.expectedRecordCount}
        - setBody:
            simple: ${body.subList(0, ${exchangeProperty.footerIndex})}
        - log:
            message: "List after removing footer (size: ${body.size})"
        #converting to material pojo
        - marshal:
            csv: {}
        - unmarshal:
            bindy:
              classType: com.example.camelRoutes.POJO.Material
              type: Csv
        - log:
            message: Output:${body}
        - setHeader:
            name: materialsList
            simple: ${body}
        #  save process file meta data

        - setHeader:
            id: setHeader-4034
            name: metadata_id
            simple:
             expression: ${bean:UUIDGeneratorBean.generateUUID}
        - setHeader:
            id: setHeader-4036
            constant: SFTP_003
            name: sftp_file_id
        - setHeader:
            id: setHeader-4037
            name: checksum
            simple: ${exchangeProperty.expectedChecksum}
        - setHeader:
            id: setHeader-4032
            name: file_name
            simple: ${exchangeProperty.fileId}
        - setHeader:
            id: setHeader-4038
            name: idempotency_key
            simple:
              expression: ${bean:UUIDGeneratorBean.generateUUID(${header.file_name})}
        - setHeader:
            id: setHeader-4039
            name: record_count
            simple: ${exchangeProperty.expectedRecordCount}
        - log:
            id: log-2774
            message: ---Insert into file meta data stars---
        - doTry:
            id: doTry-3880
            steps:
              - to:
                  id: to-2690
                  uri: sql
                  parameters:
                    dataSource: "#dataSource"
                    query: insert into file_metadata
                      (metadata_id,file_name,received_at,record_count,idempotency_key,checksum,sftp_file_id)
                      values
                      (:#metadata_id::UUID,:#file_name,now(),:#record_count::int,:#idempotency_key::UUID,:#checksum,:#sftp_file_id)
            doCatch:
              - id: doCatch-3922
                steps:
                  - throwException:
                      id: throwException-3541
                      exceptionType: java.lang.NullPointerException
                      message: "Insert failed: ${exception.message}"
                exception:
                  - java.lang.NullPointerException
            doFinally:
              id: doFinally-1724
              steps:
                - log:
                    id: log-3810
                    message: --Insert into file metadata Completed--
        - log:
            id: log-1150
            message: ---Insert into process_file table starts---
        - setHeader:
            id: setHeader-1650
            constant:
              id: status
              expression: "0"
            name: status
        - setHeader:
            id: setHeader-3639
            name: file_id
            simple:
              id: file_id
              expression: ${bean:UUIDGeneratorBean.generateUUID(${header.file_name})}
        - setHeader:
            id: setHeader-3049
            constant:
              expression: "202601081530"
            name: payroll_id
        - doTry:
            id: doTry-4154
            steps:
              - to:
                  id: to-1689
                  uri: sql
                  parameters:
                    dataSource: "#dataSource"
                    query: insert into process_file (metadata_id,
                      status,processed_at,file_id,payroll_id) values
                      (:#metadata_id::UUID,:#status::int,now(),:#file_id::UUID,:#payroll_id)
            doCatch:
              - id: doCatch-2353
                steps:
                  - throwException:
                      id: throwException-1683
                      exceptionType: java.lang.NullPointerException
                      message: "Insert failed: ${exception.message}"
                exception:
                  - java.lang.NullPointerException
            doFinally:
              id: doFinally-4112
              steps:
                - log:
                    id: log-1954
                    message: ---insert into process file completed---
        - doTry:
            id: doTry-3526
            steps:
              - to:
                  id: to-3024
                  uri: sql
                  parameters:
                    dataSource: "#dataSource"
                    query: select * from file_metadata where metadata_id = :#metadata_id::UUID
            doCatch:
              - id: doCatch-1145
                steps:
                  - throwException:
                      id: throwException-1698
                      exceptionType: java.lang.NullPointerException
                      message: "select failed: ${exception.message}"
                exception:
                  - java.lang.NullPointerException
            doFinally:
              id: doFinally-6625
              steps:
                - log:
                    id: log-1953
                    message: file meta data:${body}
        - doTry:
            id: doTry-1330
            steps:
              - to:
                  id: to-8280
                  uri: sql
                  parameters:
                    dataSource: "#dataSource"
                    query: select * from process_file where metadata_id = :#metadata_id::UUID
            doCatch:
              - id: doCatch-4159
                steps:
                  - throwException:
                      id: throwException-1614
                      exceptionType: java.lang.NullPointerException
                      message: "select failed: ${exception.message}"
                exception:
                  - java.lang.NullPointerException
            doFinally:
              id: doFinally-1202
              steps:
                - log:
                    id: log-2209
                    message: process file:${body}
        # batch processing
        - setBody:
            simple: ${headers.materialsList}
        - log:
            message: "Body: \n${body}"
        - log:
            message: "Received ${body.size} materials in the header and its type ${body.getClass()}"
        - split:
            simple: "${body}"
            steps:
              - aggregate:
                  aggregationStrategy: "#aggregationStrategy"
                  completionSize: 5
                  completionTimeout: "1000"
                  correlationExpression:
                    constant: true
                  parallelProcessing: true
                  steps:
                    - log:
                        message: "Thread ${threadName} processing batch of ${body.size} type ${body[0].getClass()}"
                    - to:
                        uri: sql
                        parameters:
                          dataSource: "#dataSource"
                          query: insert into materials (id, material, quantity, color, created_at)
                            values (:#${body.id}::UUID, :#${body.material}, :#${body.quantity}::int, :#${body.color}, now())?batch=true
                    - log:
                        message: Inserted batch of size ${body.size()}

        - log:
            message: End of the batch process

        - log:
            id: log-2481
            message: end of route
- beans:
    - name: aggregationStrategy
      type: "org.apache.camel.processor.aggregate.GroupedBodyAggregationStrategy"
