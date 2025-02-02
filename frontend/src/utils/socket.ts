import io from "socket.io-client";

import Cookies from "js-cookie";

const userId = Cookies.get("userId");

const socketUrl = process.env.NEXT_PUBLIC_SOCKET_URL || "http://localhost:8889";

const socket = io(socketUrl, {
  query: { userId: userId ?? null },
  timeout: 5000,
  transports: ["websocket"],
  reconnectionAttempts: 5,
  forceNew: true,
});

export default socket;
