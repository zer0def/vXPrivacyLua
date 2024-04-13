-- This file is part of XPrivacyLua.

-- XPrivacyLua is free software: you can redistribute it and/or modify
-- it under the terms of the GNU General Public License as published by
-- the Free Software Foundation, either version 3 of the License, or
-- (at your option) any later version.

-- XPrivacyLua is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
-- GNU General Public License for more details.

-- You should have received a copy of the GNU General Public License
-- along with XPrivacyLua.  If not, see <http://www.gnu.org/licenses/>.

-- Copyright 2017-2019 Marcel Bokhorst (M66B)

function after(hook, param)
    local result = param:getResult()
    if result == null or result:getItemCount() == 0 then
        return false
    end


    local label = param:getSetting("clipboard.label")
    local contents = param:getSetting("clipboard.contents")
    if label == nil then
        label = "l33t"
    end

    if contents == nil then
        contents = "error"
    end

    local fake = result:newPlainText(label, contents)
    param:setResult(fake)
    return true
end
