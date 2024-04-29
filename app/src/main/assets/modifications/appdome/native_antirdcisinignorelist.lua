function after(hook, param)
    local arg = param:getArgument(0)
    local res = param:getResult()
    if arg == nil or res == nil then
        return false
    end

    log("AppDome [antiRDCIsInIgnoreList] => " .. res .. " (" .. arg .. ")")
    return false
end