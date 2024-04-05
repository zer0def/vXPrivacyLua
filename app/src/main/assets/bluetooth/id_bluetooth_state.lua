function after(hook, param)
    local res = param:getResult()
    if res == nil then
        return false
    end

    local setting = param:getSetting("bluetooth.state")
    if setting == nil then
        return false
    end

    local state = tonumber(setting)
    if state == nil then
        --off
        state = 10
    end

    param:setResult(state)
    return true, tostring(res), tostring(state)
end