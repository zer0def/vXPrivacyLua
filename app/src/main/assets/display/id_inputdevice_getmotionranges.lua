function after(hook, param)
    local res = param:getResult()
    if res == nil then
        return false
    end

    local height = param:getSettingInt("display.height", 3100)
    log("InputDevice.height:" .. tostring(height))
    local xAxis = param:createXAxis(height)

    local width = param:getSettingInt("display.width", 1400)
    log("InputDevice.width:" .. tostring(width))
    local yAxis = param:createYAxis(width)

    res:set(0, xAxis)
    res:set(1, yAxis)
    param:setResult(res)
    return true
end