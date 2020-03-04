package ru.tpu.msk30.sudoku.save


class DataController {

    companion object{

        fun readData(dbHelper: ResultDataDbHelper): MutableMap<String, MutableList<Any>>{

            val map = mutableMapOf<String, MutableList<Any>>()

            val db = dbHelper.readableDatabase
            val projection = arrayOf(ResultDataContract.ResultDataEntry.COLUMN_NAME_USER_NAME, ResultDataContract.ResultDataEntry.COLUMN_NAME_TIME, ResultDataContract.ResultDataEntry.COLUMN_NAME_DIFFICULTY)
            val sortOrder = "${ResultDataContract.ResultDataEntry.COLUMN_NAME_TIME} ASC"
            val cursor = db.query(
                ResultDataContract.ResultDataEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
            )

            val userTimes = mutableListOf<Any>()
            val userNames = mutableListOf<Any>()
            val difficulties = mutableListOf<Any>()
            with(cursor){

                while(moveToNext()){
                    val name = getString(getColumnIndexOrThrow(ResultDataContract.ResultDataEntry.COLUMN_NAME_USER_NAME))
                    val difficulty = getString(getColumnIndexOrThrow(ResultDataContract.ResultDataEntry.COLUMN_NAME_DIFFICULTY))
                    val time = getLong(getColumnIndexOrThrow(ResultDataContract.ResultDataEntry.COLUMN_NAME_TIME))

                    userNames.add(name)
                    userTimes.add(time)
                    difficulties.add(difficulty)
                }

            }

            map.put("name", userNames)
            map.put("time", userTimes)
            map.put("difficulty", difficulties)


            return map
        }


    }

}