/** @jsx React.DOM */

(function () {
    var LifeWindow = React.createClass({
        render: function() {
            return <pre id="display">{this.props.data}</pre>;
        }
    });

    var ControlPanel = React.createClass({
        handleStart: function () {
            //e.preventDefault();
            var width = this.refs.width.getDOMNode().value;
            var height = this.refs.height.getDOMNode().value;
            this.props.onStart(width, height);
            //TODO set disabled, enable stop
        },
        handleStop: function() {
            this.props.onStop();
            //TODO set disabled, enable start
        },
        render: function () { return (
            <div id="footer">
                <form onSubmit={this.handleSubmit}>
                    <input type="text" id="width" ref="width" placeholder="Width..." className="input-block-level" />                
                    <input type="text" id="height" ref="height" placeholder="Height..." className="input-block-level" />
                    <input type="button" id="start" className="btn btn-primary" value="Start" onClick={this.handleStart} />
                    <input type="button" id="stop" className="btn btn-primary" value="Stop" onClick={this.handleStop} />
                </form>
            </div>
            );}
    });

    var LifeMonitor = React.createClass({
        componentWillMount: function () {
            console.log("Component will mount");
            this.listen();
        },
        getInitialState: function () {
            return { data: ""};
        },
        listen: function () {
          console.log("listen called")
          var lifeFeed;
          return function() {
              if (lifeFeed) { lifeFeed.close(); }    // if initialized, close before starting new connection
              lifeFeed = new EventSource("/lifeFeed");       // (re-)initializes connection
              lifeFeed.addEventListener("message", this.drawWorld, false);  // attach addMsg event handler
              console.log("Created listener for lifeFeed")
          }
        }(),
        drawWorld: function (msg) {
            //console.log ("Message:", msg);
            this.setState({data: JSON.parse(msg.data)}); 
        },
        handleStart: function(width, height) {
            $.ajax({
                url: "/start", 
                type: "POST", 
                data: JSON.stringify({width:width, height:height}),
                contentType:"application/json; charset=utf-8", 
                dataType:"json"//,
//                success: function (data) {
//                    console.log ("Data:", data);
//                    this.setState({data:data});
//                }.bind(this),
//                error: function (xhr, textStatus, errorThrown){
//                    console.log("error status:", textStatus, "error:", errorThrown);
//                }.bind(this)
            });
        },
        handleStop: function() {
            $.get("/stop");
        },
        render: function () { return (
            <div>
                <LifeWindow data={this.state.data}/>
                <ControlPanel onStart={this.handleStart} onStop={this.handleStop}/>
            </div>
        );}
    });

    React.render(<LifeMonitor />, document.getElementById('life-monitor'));
})();
