function after(hook, param)
    local res = param:getResult()
    if res == nil then
        return false
    end

    local height = param:getSettingInt("display.height", 3100)
    local width = param:getSettingInt("display.width", 1400)
    if height == nil or width == nil then
        return false
    end

    local xAxis = param:createXAxis(height)
    local yAxis = param:createYAxis(width)

    res:set(0, xAxis)
    res:set(1, yAxis)
    param:setResult(res)
    return true, "N/A", height .. "x" .. width
end