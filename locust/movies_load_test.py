from locust import HttpLocust, TaskSet, task
import json
import random

##locust -f movies_load_test.py --host=http://localhost:8080
class WebsiteTasks(TaskSet):
    def on_start(self):
        self.titles = ['Dirty Harry', 'The Lord of the Rings', 'The Matrix', 'Cloud Atlas', 'Beavis and Butthead', 'Avatar']
        self.descriptions = ['Alrigh', 'Splendid', 'Aweful!', 'Decent', 'Mind-altering', 'Meh']

    @task(2)
    def create(self):
        response = self.client.get("/movies/list")
        responseData = response.json()
        
        if len(responseData) >= 100:
            headers = {'content-type': 'application/json'}
            movieid = random.choice(responseData)['id']
            self.client.delete("/movies/delete", data=json.dumps(movieid), headers=headers)
        
    @task(1)
    def about(self):
        jsonData = {'title':random.choice(self.titles), 'description':random.choice(self.descriptions)}
        headers = {'content-type': 'application/json'}
        self.client.post("/movies/create", data=json.dumps(jsonData), headers=headers)
        
        


class WebsiteUser(HttpLocust):
    task_set = WebsiteTasks
    min_wait = 1000
    max_wait = 2000