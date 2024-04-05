function after(hook, param)
    local res = param:getResult()
    if res == nil then
        return false
    end

    local fake = 0
    param:setResult(fake)
    return true, tostring(res), tostring(fake)
end