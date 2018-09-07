from pymongo import MongoClient
import time
import os, sys
import random
from chai import Chai
def service_test():
    print("Test if the service work")
    
def main():
    validator=Chai()
    mongodb_url =os.environ['MONGODB_URL']
    validator.assert_not_equals(None,mongodb_url)
    client = MongoClient(mongodb_url)
    #per instruction of atlas we sleep some seconds for getting the list of nodes
    time.sleep(10)
    if len(client.nodes) <=0:
        sys.exit(0)
        #print (client.nodes)
    number_nodes =len(client.nodes)
    node_to_be_killed = random.randint(1, number_nodes)
    list_selected_nodes = random.sample(client.nodes, node_to_be_killed)
    for node in list_selected_nodes:
        print("To kill node: ", node)
        service_test()

if __name__== "__main__":
  main()
