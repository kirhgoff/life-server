class AppSpec extends FlatSpec with Matchers 
                               with BeforeAndAfterAll 
                               with ScalaFutures {
  implicit lazy val app: FakeApplication = new FakeApplication()
 
  implicit def resultToString(result: Result) =
       new String(await(
        result.body |>>> Iteratee.consume[Array[Byte]]()))
 
  "The application" should "return index.html on a GET to /" in {
    val routeMaybe = route(FakeRequest(GET, "/"))
    routeMaybe shouldBe 'defined
    whenReady(routeMaybe.get) { result =>
      resultToString(result) should include("Generate")
    }
  }
 
  override protected def beforeAll() = Play.start(app)
 
  override protected def afterAll() = Play.stop()
}