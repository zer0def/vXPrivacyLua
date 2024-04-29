function after(hook, param)
    local ths = param:getThis()
    if ths == nil then
        return false
    end

    param:setUserAgent("dummy")
    return true
end