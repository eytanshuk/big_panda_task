# big_panda_task
BigPanda Task
## thinks to improve
1.Use Redis or other in memory scalable and fast repository
2.Usage, To simplify the usage i choose to start the process with simple http get with the process name
    and start a new processor with new Repo, deleting the previous results although threads can still running.
Better solution will be handling processor and repo as Spring entity and alow only one call for running.
3.Add validation is already running and better generic way to run the process currently the Directory name is static.

**Another option to improve is to duplicate Flux and run processing of word and events in parralel

http://localhost:8080/startProcessing/{generator-windows-amd64}
expected local folder to contain the process is
C:\apps\big_panda_task\
so calling the above URL will run the process 'C:\apps\big_panda_task\generator-windows-amd64.exe' and
start processing the output.

//checking status
http://localhost:8080/displayStats