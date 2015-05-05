# alfresco-orm
Alfresco ORM is a first class persistence framework with support for relate alfresco content model and java object. 
This framework is reduce boiler plate code that required to serve Alfresco API.

1. Code for creating specific type of content/folder.
2. Setting properties to the custom/spacial content type
3. Adding association to content
4. Add aspect to the content
5. Search specific content. etc.

## Code Example

Initialised Session Factory.
```xml
<bean id="sessionFactoryORM" class="com.alfresco.orm.SessionFactory"
		init-method="init">
		<property name="serviceRegistry" ref="ServiceRegistry" />
</bean>
```
Map alfresco content model and its property to Object by using annotation.

## Motivation

Alfresco content model is very complex to understand and map to Object, that create to much of boiler plate code and very error prompting with non maintainable code. To resolve this, and create more object oriented api on top of the alfresco one can use this framework. This helps to create very maintainable and modular code.     

## Installation

Provide code examples and explanations of how to get the project.

## API Reference



## Tests

With this project one can find sample project that have test case that explain how to use this framework api.

## Contributors



## License

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
     http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.