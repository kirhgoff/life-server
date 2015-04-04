/** @jsx React.DOM */

(function () {
    var LifeWindow = React.createClass({
        render: function() {
            return <pre>{this.props.data}</pre>;
        }
    });

    var GenerateButton = React.createClass({
        handleSubmit: function () {
            //e.preventDefault();
            var width = this.refs.width.getDOMNode().value;
            var height = this.refs.height.getDOMNode().value;
            this.props.onSubmit(width, height);
        },
        render: function () { return (
            <div id="footer">
                <form onSubmit={this.handleSubmit}>
                    <input type="text" id="width" ref="width" placeholder="Width..." className="input-block-level" />                
                    <input type="text" id="height" ref="height" placeholder="Height..." className="input-block-level" />
                    <input type="button" className="btn btn-primary" value="Submit" onClick={this.handleSubmit} />
                </form>
            </div>
            );}
    });

    var LifeMonitor = React.createClass({
        getInitialState: function () {
            return { data: ""};
        },
        handleSubmit: function(width, height) {
            $.ajax({
                url: "/generateNewWorld", 
                type: "POST", 
                data: JSON.stringify({width:width, height:height}),
                contentType:"application/json; charset=utf-8", 
                dataType:"json",
                success: function (data) {
                    console.log ("Data:", data);
                    this.setState({data:data});    
                },
                error: function (xhr, textStatus, errorThrown){
                    console.log("error status:", textStatus, "error:", errorThrown);
                }
            });
        },
        render: function () { return (
            <div>
                <LifeWindow data={this.state.data}/>
                <GenerateButton name="Generate" onSubmit={this.handleSubmit}/>
            </div>
        );}
    });

    React.render(<LifeMonitor />, document.getElementById('life-monitor'));
})();
