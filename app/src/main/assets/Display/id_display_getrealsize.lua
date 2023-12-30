function after(hook, param)
    log("Display getMetrics Invoked")

    local height = param:getSettingInt("display.height", 3100)
    local width = param:getSettingInt("display.width", 1400)

    local point = param:getArgument(0)
    if point == nil then
        return false
    end

    log("Display Swapping: [P] " .. tostring(point.y) .. "x" .. tostring(point.x) .. " => " .. tostring(height) .. "x" .. tostring(width))

    point.y = height
    point.x = width

    log("Display DIM Swapped: [" .. tostring(point.y) .. "x" .. tostring(point.x)  .. "]")

    return true
end