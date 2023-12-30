function after(hook, param)
    local res = param:getResult()
    if res == nil then
        return false
    end

    local height = param:getSettingInt("display.height", 3100)
    local width = param:getSettingInt("display.width", 1400)

    log("Display Swapping: [DP] " .. tostring(res.heightPixels) .. "x" .. tostring(res.widthPixels) .. " => " .. tostring(height) .. "x" .. tostring(width))

    res.heightPixels = height
    res.widthPixels = width

    log("Display DIM Swapped: [" .. tostring(res.heightPixels) .. "x" .. tostring(res.widthPixels)  .. "]")
    param:setResult(res)
    return true
end