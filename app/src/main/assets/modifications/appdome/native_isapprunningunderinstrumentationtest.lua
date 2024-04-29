function after(hook, param)
    local res = param:getResult()
    if res == nil then
        return false
    end

    if res == true then
        log("AppDome [isAppRunningUnderInstrumentationTest] True")
        param:setResult(false)
        return true
    end

    return false
end