package com.steventran.fgodb.api

import com.google.gson.annotations.SerializedName
import kotlin.properties.Delegates

/** strengthStatus has 3 diff values
    0: This skill has not been strengthened
    1: This skill has a strengthening available, but it has not been done yet
    2: This skill has a strengthening available and it has been done
 **/
class DetailedSkill() {
    lateinit var name: String
    lateinit var description: String
    var strengthStatus by Delegates.notNull<Int>()
    lateinit var iconUrl: String
    lateinit var coolDowns: List<Int>
    lateinit var skillFunctions: List<SkillFunction>

    inner class SkillFunction() {
        lateinit var targetType: String
        lateinit var buffIconUrl: String
        lateinit var functDescrip: String
        lateinit var skillValues: List<SkillValue>

        inner class SkillValue() {
            var rate by Delegates.notNull<Int>()
            var turnCount: Int? = null // This is because turnCount is not always an attribute
            var value by Delegates.notNull<Int>()
        }

    }
}