from confluent_kafka import Producer
import threading
import time
import json
import random

conf = {
    'bootstrap.servers': "123.41.0.81:9191",
    'security.protocol': 'SASL_PLAINTEXT',
    'sasl.mechanisms': 'SCRAM-SHA-256',
    'sasl.username': 'brokersasl',
    'sasl.password': '****',
}

p = Producer(conf)

def generate_data_point(id):
    temperature = round(10 + random.random() * 170)
    if temperature > 160:
        status = "ERROR"
    elif temperature > 140 or random.randrange(1, 100) > 80:
        status = random.choice(["WARNING","ERROR"])
    else:
        status = "OK"
    return {
        'sensor_id': f"sensor-{id:02d}",
        'temperature': temperature,
        'status': status,
        'event_time': int(time.time() * 1000)
    }

def stream_data():
    try:
        count = 0
        while True:
            for id in range(100):
                if random.random() < 0.5:
                    continue
                payload = json.dumps(generate_data_point(id))
                print(payload)
                p.produce("SENSOR", key=f"{id}".encode(), value=payload.encode("utf-8"))
            p.flush()
            time.sleep(10)
    except KeyboardInterrupt:
        pass

if __name__ == '__main__':
   stream_data()
