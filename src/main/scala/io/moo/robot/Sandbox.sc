val terrain =
  """
    | oooxooo
    | oooxooo
    | oxxxxxo
    | ooooooo
  """.stripMargin


object Map {
  def apply(terrain: String) = new Map(terrain)
}

class Map(terrain: String) {
  val rows = terrain.split("\n").toList
  println(rows.max)

}
