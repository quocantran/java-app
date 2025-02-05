import io from "socket.io-client";

import Cookies from "js-cookie";

const accessToken = Cookies.get("access_token");

const socketUrl = process.env.NEXT_PUBLIC_SOCKET_URL || "http://localhost:8889";

const socket = io(socketUrl, {
  query: { accessToken: accessToken ?? null },
  timeout: 5000,
  transports: ["websocket"],
  reconnectionAttempts: 5,
  forceNew: true,
});

export default socket;
