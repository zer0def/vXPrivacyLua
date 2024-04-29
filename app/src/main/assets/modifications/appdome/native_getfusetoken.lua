function after(hook, param)
    local res = param:getResult()
    if res == nil then
        return false
    end
    log("AppDome [getFuseToken] => " .. res)
    return false
end