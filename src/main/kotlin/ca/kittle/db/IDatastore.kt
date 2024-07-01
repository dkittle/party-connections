package ca.kittle.db

interface IDatastore {

    suspend fun <T> collection() : Collection<T>

}