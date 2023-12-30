function after(hook, param)
    log("Display getMetrics Invoked")

    local height = param:getSettingInt("display.height", 3100)
    local width = param:getSettingInt("display.width", 1400)

    local displayMetrics = param:getArgument(0)
    if displayMetrics == nil then
        return false
    end

    log("Display Swapping: [DP] " .. tostring(displayMetrics.heightPixels) .. "x" .. tostring(displayMetrics.widthPixels) .. " => " .. tostring(height) .. "x" .. tostring(width))

    displayMetrics.heightPixels = height
    displayMetrics.widthPixels = width

    log("Display DIM Swapped: [" .. tostring(displayMetrics.heightPixels) .. "x" .. tostring(displayMetrics.widthPixels)  .. "]")

    return true
end