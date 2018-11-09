<img src="https://i.imgur.com/fl38v1A.png" width="60" height="60" align="right" />

# Kitty Service Broker

This demo project is made of two components:
 - a service broker, exposing [Open Service Broker API](https://www.openservicebrokerapi.org) endpoints
 using [Spring Cloud Open Service Broker](https://spring.io/projects/spring-cloud-open-service-broker),
 with a single service serving random kitty images;
 - a service consumer, using [Spring Cloud Connectors](https://cloud.spring.io/spring-cloud-connectors),
 displaying kitty images when the service is bound.

Kitty images are fetched using the [Giphy REST API](https://developers.giphy.com), implemented
using [Feign](https://github.com/OpenFeign/feign).

All these components have been tested on [Cloud Foundry](https://www.cloudfoundry.org/),
although you could deploy them on Kubernetes since OSBAPI brokers are supported on this platform.

Once the service broker is installed on your environment, you can request a Kitty service instance.
When this service is bound to your app, it contains an URL to a random kitty image, which is found
using a Giphy service call. Your app can then use this URL to show the kitty image.

<img src="https://imgur.com/download/NdIVhWy"/>

The image is refreshed each time you bind the service to your app. Give it a try!

## Prerequisites

### Get a Giphy API key

Create a developer account on [Giphy](https://developers.giphy.com) (it's free).
Get the API key and keep it handy, since it's used by the service broker to search kitty images. 

<img src="https://imgur.com/download/wsvPq9H"/>

## How to use it?

### Deploy the service broker

Compile this app using Maven & JDK 8:
```shell
$ ./mvnw clean package
```

Deploy the service broker to Cloud Foundry:
```shell
$ cf push kitty-servicebroker
```

### Configure the service broker

The service broker is exposing OSBAPI REST endpoints which require an authentication
using a HTTP Basic bearer. Set the admin password using this command:
```shell
$ cf set-env kitty-servicebroker KITTY_CREDENTIALS_ADMINPASSWORD kittyadmin
```

You also need to set the Giphy API key, and reload the service broker:
```shell
$ cf set-env kitty-servicebroker KITTY_GIPHY_APIKEY xxxxxxxxxxxxxxxxxxxxxxxxx
$ cf restage kitty-servicebroker
```

You can now register the service broker in the current space:
```shell
$ cf create-service-broker kitty admin kittyadmin http://kitty-servicebroker.apps.domain --space-scoped
Creating service broker kitty in org demos / space dev as foo...
OK
```

The service broker is available in the marketplace:
```shell
$ cf marketplace
Getting services from marketplace in org demos / space dev as foo...
OK

service                          plans                                                                      description
azure-storage                    general-purpose-storage-account*, blob-storage-account*, blob-container*   Azure Storage (Experimental)
azure-text-analytics             free, standard-s0, standard-s1, standard-s2, standard-s3, standard-s4      Azure Text Analytics (Experimental)
kitty                            standard                                                                   Access to kitty images
p-circuit-breaker-dashboard      standard                                                                   Circuit Breaker Dashboard for Spring Cloud Applications
p-config-server                  standard                                                                   Config Server for Spring Cloud Applications
p-rabbitmq                       standard                                                                   RabbitMQ service to provide shared instances of this high-performance multi-protocol messaging broker.
p-service-registry               standard                                                                   Service Registry for Spring Cloud Applications
p.mysql                          db-small, db-medium, db-large                                              Dedicated instances of MySQL
p.rabbitmq                       standard                                                                   RabbitMQ service to provide dedicated instances of th
$ cf marketplace -s kitty
Getting service plan information for service kitty as foo...
OK

service plan   description                                    free or paid
standard       Random kitty image for your viewing pleasure   free
```

The service is visible on PCF Apps Manager as well:
<img src="https://imgur.com/download/TA1Ur1Y"/>

### Create a service instance

Using this new service available in the marketplace, let's create a Kitty service instance
named `hello-kitty`:
```shell
$ cf create-service kitty standard hello-kitty
Creating service instance hello-kitty in org demos / space dev as foo...
OK
```

You may also use PCF Apps Manager:
<img src="https://imgur.com/download/I551cEv"/>

Check the service is running:
```shell
$ cf service hello-kitty
Showing info of service hello-kitty in org demos / space dev as foo...

name:            hello-kitty
service:         kitty
tags:
plan:            standard
description:     Access to kitty images
documentation:
dashboard:

Showing status of last operation from service hello-kitty...

status:    create succeeded
message:
started:   2018-11-09T23:05:00Z
updated:   2018-11-09T23:05:00Z

There are no bound apps for this service.
```

### Deploy a service consumer

We need an app to consume this service. Let's deploy it:
```shell
$ cf push kitty
```

You're done!
Go to `http://kitty.apps.domain`: your app should display a kitty image, whose URL was set
by the service instance `hello-kitty` built by the Kitty Service Broker.

If you inspect service credentials, you can see the image URL:
```shell
$ cf env kitty
System-Provided:
{
 "VCAP_SERVICES": {
  "kitty": [
   {
    "binding_name": null,
    "credentials": {
     "uri": "https://media3.giphy.com/media/rmVWkw7fogL5K/giphy.gif"
    },
    "instance_name": "hello-kitty",
    "label": "kitty",
    "name": "hello-kitty",
    "plan": "standard",
    "provider": null,
    "syslog_drain_url": null,
    "tags": [
     "kitty"
    ],
    "volume_mounts": []
   }
  ]
 }
}
```

Thanks to Spring Cloud Connectors, these credentials are interpreted by the consumer app,
and a `KittyService` instance is created. A Spring MVC controller uses this service to
build a page containing a link to this image URL.

This URL is updated each time the service is bound to an app.
Go ahead, and rebind the service again to see a new image:
```shell
$ cf unbind-service kitty hello-kitty
$ cf bind-service kitty hello-kitty
$ cf restage kitty
```

## Now, what?

Forget about kitty images, and let's think about what you could do with such
a service broker... You may use this code to expose your own services, such as:
 - private database instances
 - custom services
 - external credentials

You have access to the source code, so feel free to reuse it for your own purpose.
_Happy hacking!_

## Contribute

Contributions are always welcome!

Feel free to open issues & send PR.

## License

Copyright &copy; 2018 Pivotal Software, Inc.

This project is licensed under the [Apache Software License version 2.0](https://www.apache.org/licenses/LICENSE-2.0).
