## metric
- `metrics/jvm.memory.used`
  - ```json
    {
        "name": "jvm.memory.used",
        "description": "The amount of used memory",
        "baseUnit": "bytes",
        "measurements":
            [
                {
                    "statistic": "VALUE",
                    "value":1.29348472E8
                }
            ],
        "availableTags": 
            [
                {
                    "tag": "area",
                    "values":
                        [
                            "heap",
                            "nonheap"
                        ]
                },
                {
                    "tag": "id",
                    "values":
                        [
                            "G1 Survivor Space",
                            "Compressed Class Space",
                            "Metaspace",
                            "CodeCache",
                            "G1 Old Gen",
                            "G1 Eden Space"
                        ]
                }
            ]
    }
- `metrics/jvm.memory.used?tag=area:heap`
  - ```json
    {
        "name": "jvm.memory.used",
        "description": "The amount of used memory",
        "baseUnit": "bytes",
        "measurements":
            [
                {
                    "statistic": "VALUE",
                    "value": 5.6600576E7
                }
            ],
        "availableTags":
            [
                {
                    "tag": "id",
                    "values":
                        [
                            "G1 Survivor Space",
                            "G1 Old Gen",
                            "G1 Eden Space"
                        ]
                }
            ]
    }
    ```
- `metrics/http.server.requests?tag=uri:/log&tag=status:200`
