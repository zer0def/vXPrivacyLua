function before(hook, param)
    local arg = param:getArgument(0)
    if arg == nil then
        return false
    end
    log("AppDome [setCarrierName] => " .. arg)
    return false
end