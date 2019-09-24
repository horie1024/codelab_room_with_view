package com.horie1024.roomwordsample

import androidx.lifecycle.LiveData

class WordRepository(private val wordDao: WordDao) {

    val allWords: LiveData<List<Word>> = wordDao.getAllWords()

    suspend fun insert(word: Word) {
        wordDao.insert(word)
    }
}