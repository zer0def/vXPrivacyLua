function before(hook, param)
    local a = param:getArgument(0)
    if a == nil then
        return false
    end

    local al = string.lower(a)
    if al == "is_rooted" then
        param:setArgument(1, false)
        return true
    end

    if al == "is_emulator" then
        param:setArgument(1, false)
        return true
    end

    if al == "sms_enabled" then
       param:setArgument(1, true)
       return true
    end

    return false
end