/** @jsx React.DOM */

(function () {
    var LifeCanvas = React.createClass({
        componentDidMount: function() {
            var context = this.getDOMNode().getContext('2d');
            this.paint(context);
        },

        componentDidUpdate: function() {
            var context = this.getDOMNode().getContext('2d');
            this.paint(context);
        },

        paint: function(context) {
            var width = context.canvas.width - 2;
            var height = context.canvas.height - 2;

            var data = this.prepareData();
            var cellWidth = width/data.columns;
            var cellHeight = height/data.rows;

            context.clearRect(0, 0, width, height);

            context.fillStyle = '#9A5';
            for (var i = 0; i < data.columns; i ++) {
                for (var j = 0; j < data.rows; j ++) {
                    var value = data.cells[i][j];
                    if (this.isAlive(value)) {
                        context.fillRect(i*cellWidth, j*cellHeight, cellWidth, cellHeight);
                    }
                }
            }

            this.paintGrid(context, width, height, data.columns, data.rows, cellWidth, cellHeight);
        },

        paintGrid: function (context, width, height, columns, rows, cellWidth, cellHeight) {
            context.strokeStyle = '#9F9';
            //Vertical lines
            for (var i = 0; i <= columns; i ++) {
                var x = i * cellWidth;
                context.beginPath();
                context.moveTo(x, 0);
                context.lineTo(x, height);
                context.stroke();
            }

            //Horizontal lines
            for (var i = 0; i <= rows; i ++) {
                var y = i * cellHeight;
                context.beginPath();
                context.moveTo(0, y);
                context.lineTo(height, y);
                context.stroke();
            }
        },

        isAlive: function (value) {return value == '*';},

        prepareData: function () {
            var data = this.props.data;
            console.log ("Data is: ", data);
            console.log ("data[0]:", data[0]);
            return {columns:data[0].length, rows:data.length, cells:data};
        },

        render: function() {
            return <canvas id="lifeCanvas" width={200} height={200} />;
        }
    });

    //---------------------------------------------------------------

    var LifeWindow = React.createClass({
        render: function() {
            return <pre id="display">{this.props.data}</pre>;
        }
    });

    //---------------------------------------------------------------

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
                </form>
            </div>
            );}
    });

    //---------------------------------------------------------------

    var LifeMonitor = React.createClass({
        componentWillMount: function () {
            console.log("Component will mount");
            this.listen();
        },
        getInitialState: function () {
            return { data: [
                    [' ', '*', '_'],
                    [' ', '*', '*'],
                    ['*', '*', '_']
                ]
            };
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
            var serverData = JSON.parse(msg.data).content;
            console.log ("serverData:", serverData);
            var array2d = serverData.split("\n").map(function(e) {
                return e.split("");
            })
            console.log ("data split:", array2d);
            this.setState({data: array2d});
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
                <LifeCanvas data={this.state.data}/>
                <ControlPanel onStart={this.handleStart} onStop={this.handleStop}/>
            </div>
        );}
    });

    React.render(<LifeMonitor />, document.getElementById('life-monitor'));
})();
