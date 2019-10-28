# Github User Repositories App
Github Api - Apollo(GraphQL) - Kotlin/Java

## Prerequisites
- git
- JDK 8 (Java)
- Github token

## Generate Github Token
1. Visit [https://github.com/settings/tokens](https://github.com/settings/tokens), click in **Generate new token**, select option **repo**.

2. Click **Generate token**.

3. Copy the generated string.

## Config
Please navigate to [app/build.gradle](https://github.com/julitus/GQL-github/blob/master/app/build.gradle) open and paste your Github Token. Do not delete (\"), they are necessary.
```sh
	buildTypes.each {
	    it.buildConfigField('String', "AUTH_TOKEN", "\"toke_here\"")
	} 
```