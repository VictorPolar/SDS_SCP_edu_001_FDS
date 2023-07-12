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
    amount = round(10 + random.random() * 2000)
    if amount > 1900:
        status = "ERROR"
    elif amount > 1600 or random.randrange(1, 1000) > 800:
        status = random.choice(["WARNING","ERROR"])
    else:
        status = "OK"
    return {
        'customer_id': f"customer-{id:02d}",
        'amount': amount,
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
                p.produce("FDS", key=f"{id}".encode(), value=payload.encode("utf-8"))
            p.flush()
            time.sleep(10)
    except KeyboardInterrupt:
        pass

if __name__ == '__main__':
   stream_data()
