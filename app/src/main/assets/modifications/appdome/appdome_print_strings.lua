function after(hook, param)
    local res = param:getResult()
    if res == nil then
        return false
    end

    log("APD [string] => " .. res)
    return false
end