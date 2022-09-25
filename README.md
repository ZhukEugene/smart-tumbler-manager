# smart-tumbler-manager

## endpoints:
GET http://localhost:8080/get_schedule <br />
GET http://localhost:8080/clear_schedule <br />
POST http://localhost:8080/post_schedule

### post_schedule body JSON Examples:
```json
[
    {
        "deviceID": "1",
        "date": "2022-09-25",
        "actions": [
            {
                "timeOn": "23:47:00",
                "timeOff": "21:45:10"
            },
            {
                "timeOn": "21:49:00",
                "timeOff": "23:51:15"
            }
        ]
    },
    {
        "deviceID": "2",
        "date": "2022-09-25",
        "actions": [
            {
                "timeOn": "22:56:30",
                "timeOff": "21:55:30"
            },
            {
                "timeOn": "22:53:30",
                "timeOff": "22:53:35"
            }
        ]
    }
]
```
```json
[
    {
        "deviceID": "1",
        "date": "2022-09-25",
        "actions": [
            {
                "timeOn": "22:52:00",
                "timeOff": "22:51:10"
            }
        ]
    }
]
```