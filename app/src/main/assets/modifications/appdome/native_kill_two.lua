function before(hook, param)
    log("AppDome [killProcess]")
    param:setResult(true)
    return true
end