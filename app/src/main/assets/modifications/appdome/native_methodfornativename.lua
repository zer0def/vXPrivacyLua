function after(hook, param)
    local res = param:getResult()
    local arg = param:getArgument(0)
    if res == nil or arg == nil then
        return false
    end
    log("AppDome [methodForNameNative] => " .. res .. " (" .. arg .. ")")
    return false
end