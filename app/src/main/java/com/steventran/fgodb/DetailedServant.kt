package com.steventran.fgodb

import com.steventran.fgodb.api.DetailedSkill
import com.steventran.fgodb.api.CharacterSaintGraphs
import kotlin.properties.Delegates

class DetailedServant(){
    var collectionNo by Delegates.notNull<Int>()
    lateinit var servantName: String
    lateinit var className: String
    var rarity by Delegates.notNull<Int>()
    lateinit var  type: String
    lateinit var  characterAscensionUrls: List<String>
    lateinit var  skills: List<DetailedSkill>
    var atkBase by Delegates.notNull<Int>()
    var atkMax by Delegates.notNull<Int>()
    var hpBase by Delegates.notNull<Int>()
    var hpMax by Delegates.notNull<Int>()
}