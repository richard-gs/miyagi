# miyagi

Run with `grails run-app`

## Troubleshooting
---
If you see 
```
Execution failed for task ':findMainClass'.
> Unable to find a single main class from the following candidates XYZ
```

Then run `./gradlew clean`

---
Don't name any tables "User"

---
If nothing new appears in the DB after .save() then try flushing

---
[Optional Parenthesis](https://stackoverflow.com/questions/27857391/groovy-function-call-omiting-the-parentheses/27857825)  
`respond ['ok']` vs `respond(['ok'])`  
Also you cannot chain commands with omitted parentheses
