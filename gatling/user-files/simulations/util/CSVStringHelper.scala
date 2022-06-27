package util

object CSVStringHelper {
  def join (valueList: List[String]): String = {
    "\"" + valueList.mkString("\",\"") + "\""
  }
}
