# JSON-RPC

JSON-RPC service, with spring and jsonrpc4j.

Accept json POST to /service endpoint.
Excepted method names: countWorkingSensors, wrappedCountWorkingSensors.

Accept POST json example:
[
    {
        "id": "1",
        "jsonrpc": "2.0",
        "method": "countWorkingSensors",
        "params": {
            "data": [
                {
                    "id_sample": "76rtw",
                    "num_id": "ffg#er111",
                    "id_location": "Екатеринбург",
                    "id_signal_par": "0xcv11cs1",
                    "id_detected": "None",
                    "id_class_det": "req111"
                },
                {
                    "id_sample": "76rtw2",
                    "num_id": "ffg#er112",
                    "id_location": "Сочи",
                    "id_signal_par": "0xcv11cs2",
                    "id_detected": "None",
                    "id_class_det": "req112"
                },
                {
                    "id_sample": "76rtw3",
                    "num_id": "ffg#er113",
                    "id_location": "Екатеринбург",
                    "id_signal_par": "0xcv11cs3",
                    "id_detected": "Nan",
                    "id_class_det": "req113"
                }
            ]
        }
    }
]

countWorkingSensors method will return : 
[
    {
        "jsonrpc": "2.0",
        "id": "1",
        "result": [
            {
                "location": "Екатеринбург",
                "numberOfOkSensors": 1,
                "numberOfBrokenSensors": 1
            },
            {
                "location": "Сочи",
                "numberOfOkSensors": 1,
                "numberOfBrokenSensors": 0
            }
        ]
    }
]

wrappedCountWorkingSensors will return:
[
    {
        "jsonrpc": "2.0",
        "id": "1",
        "result": {
            "data": [
                {
                    "location": "Екатеринбург",
                    "numberOfOkSensors": 1,
                    "numberOfBrokenSensors": 1
                },
                {
                    "location": "Сочи",
                    "numberOfOkSensors": 1,
                    "numberOfBrokenSensors": 0
                }
            ]
        }
    }
]

