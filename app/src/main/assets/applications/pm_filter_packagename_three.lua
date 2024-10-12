function after(hook, param)
    local res = param:getResult()
    if res == nil then
        return false
    end

    local p = param:getArgument(0)
    if p == nil then
        return false
    end

    local good = param:listHasString("applications.allow.list", p)
    if good == true then
        return false
    end

    param:setResult(null)
    return true
end