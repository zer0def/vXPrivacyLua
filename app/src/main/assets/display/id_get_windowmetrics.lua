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

    local oRect = res:getBounds()
    local old = tostring(oRect.height()) .. "x" .. tostring(oRect.width())
    local new = tostring(height) .. "x" .. tostring(width)
    local rect = luajava.newInstance("android.graphics.Rect", 0, 0, width, height)
    local wMetric = luajava.newInstance("android.view.WindowMetrics", rect, res:getWindowInsets())
    param:setResult(wMetric)
    return true, old, new
end


--     */
--    public final int width() {
--        return right - left;
--    }
--    /**
--     * @return the rectangle's height. This does not check for a valid rectangle
--     * (i.e. top <= bottom) so the result may be negative.
--     */
--    public final int height() {
--        return bottom - top;
--    }