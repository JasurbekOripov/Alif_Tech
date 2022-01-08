package uz.juo.domain.models

data class BookData(
    val `data`: List<Data> = emptyList(),
    val total: String= ""
)