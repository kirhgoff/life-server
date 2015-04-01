/** @jsx React.DOM */

(function () {
    var LifeWindow = React.createClass({
        render: function() {
            var msgNodes = this.props.data.map(function (msg) {
                return <ChatMsg user={msg.user} time={msg.time} text={msg.text} name={this.props.name} />;
            }.bind(this));
            return <div id="chat">{msgNodes}</div>;
        }
    });

    var GenerateButton = React.createClass({
        handleSubmit: function () {
            var msg = { text: this.refs.text.getDOMNode().value, user: this.props.name,
                time: (new Date()).toUTCString(), room: "room" + this.props.room };
            console.log(msg)
            $.ajax({url: "/chat", type: "POST", data: JSON.stringify(msg),
                contentType:"application/json; charset=utf-8", dataType:"json"});
            this.refs.text.getDOMNode().value = ""; // empty text field
            return false;
        },
        render: function () { return (
            <div id="footer">
                <form onSubmit={this.handleSubmit}>
                    <input type="text" id="textField" ref="text" placeholder="Say something" className="input-block-level" />
                    <input type="button" className="btn btn-primary" value="Submit" onClick={this.handleSubmit} />
                </form>
            </div>
            );}
    });

    var LifeMonitor = React.createClass({
        render: function () { return (
            <div>
                <LifeWindow/>
                <GenerateButton name={this.state.name} />
            </div>
            );}
    });

    /** render top-level ChatApp component */
    React.renderComponent(<LifeMonitor />, document.getElementById('life-monitor'));
})();
