function before(hook, param)
    log("AppDome [killForkUtilChildProcesses]")
    param:setResult(null)
    return true
end