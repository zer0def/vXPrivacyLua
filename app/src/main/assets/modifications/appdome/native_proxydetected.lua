function before(hook, param)
    local p = param:getArgument(0)
    if p == nil then
        return false
    end
    log("AppDome [proxyDetected] => " .. p)
    return false
end