function before(hook, param)
    local ths = param:getThis()
    if ths == nil then
        return false
    end

    local res = false
    local name = param:getSetting("account.user.name")
    if name ~= nil then
        param:setArgument(1, name)
        res = true
    end

    local id = param:getSetting("account.user.id")
    if id ~= nil then
        if param:isNumericString(id) then
            param:setArgument(0, tonumber(id))
            res = true
        end
    end

    return res
end